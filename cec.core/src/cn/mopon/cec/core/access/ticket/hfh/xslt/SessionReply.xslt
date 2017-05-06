<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/data">
		<data>
			<result><xsl:value-of select="@result" /></result>
			<message><xsl:value-of select="@message" /></message>
			<xsl:variable name="cinemaCode" select="shows/@cinemaId" />
			<shows>
				<xsl:for-each select="shows/show">
					<xsl:variable name="hallCode" select="@hallId" />
					<xsl:variable name="time"
						select="concat(substring('0000', string-length(@time)+1), @time)" />
					<Show>
						<xsl:variable name="type" select="substring(film/@id,4,1)"></xsl:variable>
						<cinema>
							<code><xsl:value-of select="$cinemaCode" /></code>
						</cinema>
						<hall>
							<code><xsl:value-of select="$hallCode" /></code>
							<cinema>
								<code><xsl:value-of select="$cinemaCode" /></code>
							</cinema>
						</hall>
						<filmCode><xsl:value-of select="film/@id" /></filmCode>
						<language><xsl:value-of select="film/@language" /></language>
						<showType>
							<xsl:choose>
								<xsl:when test="$type='1'">1</xsl:when>
								<xsl:when test="$type='2'">2</xsl:when>
								<xsl:when test="$type='3'">3</xsl:when>
								<xsl:when test="$type='4'">4</xsl:when>
								<xsl:when test="$type='6'">6</xsl:when>
								<xsl:when test="$type='8'">6</xsl:when>
								<xsl:when test="$type='9'">6</xsl:when>
								<xsl:when test="$type='i'">6</xsl:when>
								<xsl:when test="$type='j'">6</xsl:when>
								<xsl:when test="$type='C'">6</xsl:when>
								<xsl:when test="$type='D'">6</xsl:when>
								<xsl:when test="$type='b'">1</xsl:when>
								<xsl:when test="$type='c'">2</xsl:when>
								<xsl:when test="$type='m'">2</xsl:when>
								<xsl:when test="$type='w'">2</xsl:when>
								<xsl:when test="$type='d'">3</xsl:when>
								<xsl:when test="$type='n'">3</xsl:when>
								<xsl:when test="$type='x'">3</xsl:when>
								<xsl:when test="$type='e'">4</xsl:when>
								<xsl:when test="$type='o'">4</xsl:when>
								<xsl:when test="$type='y'">4</xsl:when>
								<xsl:otherwise>1</xsl:otherwise>
							</xsl:choose>
						</showType>
						<code><xsl:value-of select="@seqNo" />_<xsl:value-of select="@showSeqNo" /></code>
						<showTime><xsl:value-of select="@date" />-<xsl:value-of select="$time" /></showTime>
						<through>
							<xsl:choose>
								<xsl:when test="@flag = 'Y'">true</xsl:when>
								<xsl:otherwise>false</xsl:otherwise>
							</xsl:choose>
						</through>
						<minPrice><xsl:value-of select="price/@lowest" /></minPrice>
						<stdPrice><xsl:value-of select="price/section[1]/@standard" /></stdPrice>
						<duration><xsl:value-of select="film/@duration" /></duration>
					</Show>
				</xsl:for-each>
			</shows>
		</data>
	</xsl:template>
</xsl:stylesheet>