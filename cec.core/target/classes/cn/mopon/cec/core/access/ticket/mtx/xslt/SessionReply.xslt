<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/GetCinemaPlanResult">
		<GetCinemaPlanResult>
			<ResultCode><xsl:value-of select="ResultCode" /></ResultCode>
			<shows>
				<xsl:for-each select="CinemaPlans/CinemaPlan">
					<xsl:variable name="code" select="FilmNo" />
					<xsl:variable name="showType" select="substring($code,4,1)" />
					<Show>
						<cinema>
							<code><xsl:value-of select="PlaceNo" /></code>
						</cinema>
						<hall>
							<code><xsl:value-of select="HallNo" /></code>
							<cinema>
								<code><xsl:value-of select="PlaceNo" /></code>
							</cinema>
						</hall>
						<code><xsl:value-of select="FeatureAppNo" /></code>
						<status><xsl:value-of select="SetClose" /></status>
						<filmCode><xsl:value-of select="$code" /></filmCode>
						<showTime>
							<xsl:value-of select="FeatureDate" />
							<xsl:value-of select="FeatureTime" />
						</showTime>
						<startTime>
							<xsl:value-of select="FeatureDate" />
							<xsl:value-of select="FeatureTime" />:00
						</startTime>
						<endTime>
							<xsl:value-of select="FeatureDate" />
							<xsl:value-of select="TotalTime" />:00
						</endTime>
						<through>false</through>
						<stdPrice><xsl:value-of select="StandPric" /></stdPrice>
						<language><xsl:value-of select="CopyLanguage" /></language>
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
		</GetCinemaPlanResult>
	</xsl:template>
</xsl:stylesheet>