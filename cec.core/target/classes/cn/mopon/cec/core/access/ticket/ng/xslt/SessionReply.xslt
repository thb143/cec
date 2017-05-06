<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/OnlineTicketingServiceReply/DQuerySessionReply/Sessions">
		<xsl:variable name="cinemaCode" select="@CinemaCode" />
		<shows>
			<xsl:for-each select="Session">
				<xsl:variable name="hallCode" select="@ScreenCode" />
				<xsl:variable name="showType" select="substring(Films/Film[1]/Code,4,1)" />
				<Show>
					<cinema>
						<code><xsl:value-of select="$cinemaCode" /></code>
					</cinema>
					<hall>
						<code><xsl:value-of select="$hallCode" /></code>
					</hall>
					<code><xsl:value-of select="Code" /></code>
					<filmCode><xsl:value-of select="Films/Film[1]/Code" /></filmCode>
					<showTime><xsl:value-of select="StartTime" /></showTime>
					<through>
						<xsl:choose>
							<xsl:when test="PlaythroughFlag='Yes'">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</through>
					<minPrice><xsl:value-of select="Price/LowestPrice" /></minPrice>
					<stdPrice><xsl:value-of select="Price/GuidePrice" /></stdPrice>
					<language><xsl:value-of select="Films/Film[1]/Language"/></language>
					<duration><xsl:value-of select="Films/Film[1]/Duration"/></duration>
					<showType>
						<xsl:choose>
							<xsl:when test="$showType='1'">1</xsl:when>
							<xsl:when test="$showType='2'">2</xsl:when>
							<xsl:when test="$showType='3'">3</xsl:when>
							<xsl:when test="$showType='4'">4</xsl:when>
							<xsl:when test="$showType='6'">6</xsl:when>
							<xsl:when test="$showType='8'">6</xsl:when>
							<xsl:when test="$showType='9'">6</xsl:when>
							<xsl:when test="$showType='i'">6</xsl:when>
							<xsl:when test="$showType='j'">6</xsl:when>
							<xsl:when test="$showType='C'">6</xsl:when>
							<xsl:when test="$showType='D'">6</xsl:when>
							<xsl:when test="$showType='b'">1</xsl:when>
							<xsl:when test="$showType='c'">2</xsl:when>
							<xsl:when test="$showType='m'">2</xsl:when>
							<xsl:when test="$showType='w'">2</xsl:when>
							<xsl:when test="$showType='d'">3</xsl:when>
							<xsl:when test="$showType='n'">3</xsl:when>
							<xsl:when test="$showType='x'">3</xsl:when>
							<xsl:when test="$showType='e'">4</xsl:when>
							<xsl:when test="$showType='o'">4</xsl:when>
							<xsl:when test="$showType='y'">4</xsl:when>
							<xsl:otherwise>1</xsl:otherwise>
						</xsl:choose>
					</showType>
				</Show>
			</xsl:for-each>
		</shows>
	</xsl:template>
</xsl:stylesheet>