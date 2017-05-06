<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/reply">
		<data>
			<code><xsl:value-of select="code" /></code>
			<msg><xsl:value-of select="msg" /></msg>
			<shows>
				<xsl:for-each select="shows/show">
					<show>
						<code><xsl:value-of select="channelShowCode" /></code>
						<cinema>
							<code><xsl:value-of select="cinemaCode" /></code>
						</cinema>
						<hall>
							<code><xsl:value-of select="hallCode" /></code>
						</hall>
						<filmCode><xsl:value-of select="filmCode" /></filmCode>
						<showType>
							<xsl:choose>
								<xsl:when test="showType='1'">1</xsl:when>
								<xsl:when test="showType='2'">2</xsl:when>
								<xsl:when test="showType='3'">3</xsl:when>
								<xsl:when test="showType='4'">4</xsl:when>
								<xsl:when test="showType='6'">6</xsl:when>
								<xsl:when test="showType='8'">6</xsl:when>
								<xsl:when test="showType='9'">6</xsl:when>
								<xsl:when test="showType='i'">6</xsl:when>
								<xsl:when test="showType='j'">6</xsl:when>
								<xsl:when test="showType='C'">6</xsl:when>
								<xsl:when test="showType='D'">6</xsl:when>
								<xsl:when test="showType='b'">1</xsl:when>
								<xsl:when test="showType='c'">2</xsl:when>
								<xsl:when test="showType='m'">2</xsl:when>
								<xsl:when test="showType='w'">2</xsl:when>
								<xsl:when test="showType='d'">3</xsl:when>
								<xsl:when test="showType='n'">3</xsl:when>
								<xsl:when test="showType='x'">3</xsl:when>
								<xsl:when test="showType='e'">4</xsl:when>
								<xsl:when test="showType='o'">4</xsl:when>
								<xsl:when test="showType='y'">4</xsl:when>
								<xsl:otherwise>1</xsl:otherwise>
							</xsl:choose>
						</showType>
						<language><xsl:value-of select="language" /></language>
						<duration><xsl:value-of select="duration"/></duration>
						<showTime><xsl:value-of select="showTime" /></showTime>
						<minPrice><xsl:value-of select="minPrice" /></minPrice>
						<stdPrice><xsl:value-of select="stdPrice" /></stdPrice>
						<status><xsl:value-of select="status" /></status>
						<through>0</through>
					</show>
				</xsl:for-each>
			</shows>
		</data>
	</xsl:template>
</xsl:stylesheet>